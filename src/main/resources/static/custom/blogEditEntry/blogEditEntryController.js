/**
 * Created by DruidKuma on 7/21/16.
 */
angular.module("blogApp")
    .controller('BlogEditEntryController',['$scope', 'BlogEntry', '$routeParams', '$sce', '$location', function($scope, BlogEntry, $routeParams, $sce, $location) {

        $scope.loadBlogPost = function() {
            if($routeParams.id) {
                $scope.loadingProcess = true;
                BlogEntry.single($routeParams.id).then(function(response) {
                    $scope.postEntry = response.data;
                    $scope.pageHeading.title = "Blog Entry Editor";
                    $scope.editEntryModel = $scope.postEntry.content;

                }).finally(function() {
                    $scope.loadingProcess = false;
                });
            }
        };

        $scope.loadBlogPost();
    }]);