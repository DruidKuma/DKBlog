angular.module("blogApp")
    .controller('BlogEntryController',['$scope', 'BlogEntry', '$routeParams', '$sce', function($scope, BlogEntry, $routeParams, $sce) {
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

        $scope.switchPublishStatus = function() {
            BlogEntry.switchPublishStatus($scope.postEntry.id).then(function() {
                $scope.postEntry.isPublished = !$scope.postEntry.isPublished;
            });
        };

        $scope.loadBlogPost();
    }]);