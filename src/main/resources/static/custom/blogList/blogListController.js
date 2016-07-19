/**
 * Created by DruidKuma on 7/12/16.
 */
angular.module("blogApp")
    .controller('BlogListController',['$scope', '$rootScope', 'BlogEntry', function($scope, $rootScope, BlogEntry) {
        $scope.$on('$routeChangeSuccess', function () {
            $rootScope.pageTitle = "All posts";
        });

        $rootScope.loadingProcess = false;
        $scope.numPageSize = 5;

        $scope.blogListFilter = {
            currentPage: 1,
            entriesOnPage: 10,
            category: '',
            sort: 'creationDate DESC',
            search: '',
            filterPublished: '',
            totalItems: 0
        };

        $scope.reloadBlogPosts = function() {
            BlogEntry.page($scope.blogListFilter).then(function(response) {
                $scope.posts = response.data.content;
                $scope.blogListFilter.totalItems = response.data.totalElements;
            });
        };

        $scope.reloadBlogPosts();
    }]);

