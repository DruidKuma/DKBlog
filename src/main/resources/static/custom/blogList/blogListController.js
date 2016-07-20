/**
 * Created by DruidKuma on 7/12/16.
 */
angular.module("blogApp")
    .controller('BlogListController',['$scope', '$rootScope', 'BlogEntry', function($scope, $rootScope, BlogEntry) {

        //Page Heading
        $scope.$on('$routeChangeSuccess', function () {
            $rootScope.pageTitle = "All posts";
        });

        // Blog List Filter Options
        $scope.filterChanged = false;
        $scope.numPageSize = 5;
        $scope.entriesOnPageOptions = [10, 20, 50, 100];
        $scope.sortOptions = [
            {value: 'creationDate DESC', name: 'Date (Latest first)'},
            {value: 'creationDate ASC', name: 'Date (Oldest first)'},
            {value: 'content.title ASC', name: 'Title (From A to Z)'},
            {value: 'content.title DESC', name: 'Title (From Z to A)'}
        ];
        $scope.publishFilter = [
            {value: '', name: 'All posts'},
            {value: 'true', name: 'Only published'},
            {value: 'false', name: 'Only not published'}
        ];

        // Helper Functions
        $scope.reloadWithCustomFilter = function() {
            $scope.filterChanged = true;
            $scope.reloadBlogPosts();
        };

        $scope.resetPaginationAndReload = function() {
            $scope.filterChanged = true;
            $scope.resetPagination();
            $scope.reloadBlogPosts();
        };

        $scope.resetPagination = function() {
            $scope.blogListFilter.currentPage = 1;
            $scope.blogListFilter.totalItems = 0;
        };

        $scope.resetBlogListFilter = function() {
            $scope.blogListFilter.currentPage = 1;
            $scope.blogListFilter.entriesOnPage = 10;
            $scope.blogListFilter.category = '';
            $scope.blogListFilter.sort = 'creationDate DESC';
            $scope.blogListFilter.totalItems = 0;
            $scope.blogListFilter.filterPublished = '';
            $scope.filterChanged = false;
            $scope.reloadBlogPosts();
        };

        // Initialize
        $scope.resetBlogListFilter();
        $scope.reloadBlogPosts();
    }]);

