/**
 * Created by DruidKuma on 7/12/16.
 */
angular.module("blogApp")
    .controller('BlogListController',['$scope', '$routeParams', '$translatePartialLoader', '$translate', 'Category', function($scope, $routeParams, $translatePartialLoader, $translate, Category) {

        //Page Heading
        $scope.$on('$routeChangeSuccess', function () {
            $scope.pageHeading.title = "All posts";
            $translatePartialLoader.addPart('components.category');
            $translate.refresh();

            Category.simple().then(function(response) {
                $scope.categoryOptions = response.data;
                $scope.categoryOptions.unshift({
                    nameKey: 'All posts'
                });
            });
        });

        // Blog List Filter Options
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
            $scope.blogListFilter.filterChanged = true;
            $scope.reloadBlogPosts();
        };

        $scope.resetPaginationAndReload = function() {
            $scope.blogListFilter.filterChanged = true;
            $scope.resetPagination();
            $scope.reloadBlogPosts();
        };

        $scope.resetPagination = function() {
            $scope.blogListFilter.currentPage = 1;
            $scope.blogListFilter.totalItems = 0;
        };

        $scope.resetBlogListFilter = function(partialReload) {
            $scope.blogListFilter.currentPage = 1;
            $scope.blogListFilter.entriesOnPage = 10;
            $scope.blogListFilter.category = 'All posts';
            $scope.blogListFilter.sort = 'creationDate DESC';
            $scope.blogListFilter.totalItems = 0;
            $scope.blogListFilter.filterPublished = '';

            if(!partialReload) $scope.blogListFilter.search = '';
            $scope.blogListFilter.filterChanged = partialReload;

            $scope.reloadBlogPosts();
        };

        $scope.$on('countryChanged', function(event, data) {
            $scope.resetBlogListFilter();
        });

        // Initialize
        $scope.resetBlogListFilter($routeParams.partial);
        $scope.reloadBlogPosts();
    }]);

