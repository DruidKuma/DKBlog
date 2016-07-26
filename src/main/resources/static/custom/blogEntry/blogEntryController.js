angular.module("blogApp")
    .controller('BlogEntryController',['$scope', 'BlogEntry', '$routeParams', '$sce', '$location', function($scope, BlogEntry, $routeParams, $sce, $location) {

        $scope.loadBlogPost = function() {
            $scope.loadingProcess = true;
            BlogEntry.single($routeParams.id).then(function(response) {
                $scope.postEntry = response.data;
                $scope.pageHeading.title = $scope.postEntry.title;
                $scope.postEntry.content = $sce.trustAsHtml($scope.postEntry.content);
            }).finally(function() {
                $scope.loadingProcess = false;
            });
        };

        $scope.switchPublishStatus = function() {
            BlogEntry.switchPublishStatus($scope.postEntry.id).then(function() {
                $scope.postEntry.isPublished = !$scope.postEntry.isPublished;
            });
        };

        $scope.deletePost = function() {
            BlogEntry.deletePost($scope.postEntry.id).then(function() {
                $location.path("/list");
            });
        };

        $scope.loadBlogPost();
    }]);