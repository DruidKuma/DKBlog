/**
 * Created by DruidKuma on 7/20/16.
 */
angular.module("blogApp")
    .controller('PageWrapperController',['$scope', 'BlogEntry', function($scope, BlogEntry) {
        $scope.pageHeading = {};
        $scope.blogListFilter = {
            search: '',
            filterChanged: false
        };

        $scope.reloadBlogPosts = function() {
            $scope.loadingProcess = true;
            BlogEntry.page($scope.blogListFilter).then(function(response) {
                $scope.posts = response.data.content;
                $scope.blogListFilter.totalItems = response.data.totalElements;
            }).finally(function() {
                $scope.loadingProcess = false;
            });
        };
        $scope.reloadForSearch = function() {
            $scope.blogListFilter.filterChanged = true;
            $scope.reloadBlogPosts();
        }
    }]);
