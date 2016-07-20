/**
 * Created by DruidKuma on 7/20/16.
 */
angular.module("blogApp")
    .controller('PageWrapperController',['$scope', '$rootScope', 'BlogEntry', function($scope, $rootScope, BlogEntry) {

        $scope.blogListFilter = {
            search: ''
        };

        $scope.reloadBlogPosts = function() {
            $rootScope.loadingProcess = true;
            BlogEntry.page($scope.blogListFilter).then(function(response) {
                $scope.posts = response.data.content;
                $scope.blogListFilter.totalItems = response.data.totalElements;
            }).finally(function() {
                $rootScope.loadingProcess = false;
            });
        };
    }]);
