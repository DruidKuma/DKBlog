angular.module("blogApp")
    .controller('BlogEntryController',['$scope', 'BlogEntry', '$routeParams', '$sce',
        function($scope, BlogEntry, $routeParams, $sce) {
            $scope.$on('$routeChangeSuccess', function () {
                $("input[data-role=tagsinput], select[multiple][data-role=tagsinput]").tagsinput();
            });

            $scope.loadBlogPost = function() {
                $scope.loadingProcess = true;
                BlogEntry.single($routeParams.id).then(function(response) {
                    $scope.postEntry = response.data;
                    $scope.pageHeading.title = $scope.postEntry.title;
                    $scope.postEntry.content = $sce.trustAsHtml($scope.postEntry.content);
                }, function(error) {
                    //    TODO
                }).finally(function() {
                    $scope.loadingProcess = false;
                });
            };

            $scope.loadBlogPost();

    }])
    .factory('RecursionHelper', ['$compile', function($compile){
        return {
            /**
             * Manually compiles the element, fixing the recursion loop.
             * @param element
             * @param [link] A post-link function, or an object with function(s) registered via pre and post properties.
             * @returns An object containing the linking functions.
             */
            compile: function(element, link){
                // Normalize the link parameter
                if(angular.isFunction(link)){
                    link = { post: link };
                }

                // Break the recursion loop by removing the contents
                var contents = element.contents().remove();
                var compiledContents;
                return {
                    pre: (link && link.pre) ? link.pre : null,
                    /**
                     * Compiles and re-adds the contents
                     */
                    post: function(scope, element){
                        // Compile the contents
                        if(!compiledContents){
                            compiledContents = $compile(contents);
                        }
                        // Re-add the compiled contents to the element
                        compiledContents(scope, function(clone){
                            element.append(clone);
                        });

                        // Call the post-linking function, if any
                        if(link && link.post){
                            link.post.apply(null, arguments);
                        }
                    }
                };
            }
        };
    }])
    .directive("baComment", function (RecursionHelper) {
        return {
            restrict: "E",
            scope: {
                comment: "="
            },
            compile: function(elem){
                return RecursionHelper.compile(elem);
            },
            template: "<div style='margin-top:15px' class=\"media m-b-10\">\n    <div class=\"media-left\">\n        <a href=\"#\">\n            <img class=\"media-object img-circle thumb-sm\" alt=\"64x64\" src=\"/dist/images/users/avatar-3.jpg\">\n        </a>\n    </div>\n    <div class=\"media-body\">\n        <h4 class=\"media-heading\">{{comment.author}}</h4>\n        <p class=\"font-13 text-muted m-b-0\">\n            {{comment.body}}\n        </p>\n        <a href=\"\" class=\"text-success font-13\">Reply</a>\n        <ba-comment comment=\'child\' ng-repeat=\'child in comment.children\'></ba-comment>\n    </div>\n</div>"
        }
    });