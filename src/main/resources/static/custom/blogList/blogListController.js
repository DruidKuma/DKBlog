/**
 * Created by DruidKuma on 7/12/16.
 */
angular.module("blogApp")
    .controller('BlogListController',['$scope', '$rootScope', 'BlogEntry', function($scope, $rootScope, BlogEntry) {
        $scope.$on('$routeChangeSuccess', function () {
            $rootScope.pageTitle = "All posts";
        });

        $scope.numPageSize = 5;
        $scope.numEntriesOptions = [10, 20, 50, 100];
        $scope.sortOptions = [
            {value: 'creationDate DESC', name: 'Date (Latest first)'},
            {value: 'creationDate ASC', name: 'Date (Oldest first)'},
            {value: 'content.title ASC', name: 'Title (From A to Z))'},
            {value: 'content.title DESC', name: 'Title (From Z to A))'}
        ];
        $scope.publishOptions = [
            {value: '', name: 'All posts'},
            {value: 'true', name: 'Only published'},
            {value: 'false', name: 'Only not published'}
        ];

        $scope.blogListFilter = {
            currentPage: 1,
            entriesOnPage: 10,
            category: '',
            sort: 'creationDate DESC',
            search: $rootScope.searchString,
            filterPublished: '',
            totalItems: 0
        };

        $scope.reloadBlogPosts = function() {
            $rootScope.loadingProcess = true;

            BlogEntry.page($scope.blogListFilter)
                .then(function(response) {
                    $scope.posts = response.data.content;
                    $scope.blogListFilter.totalItems = response.data.totalElements;
                })
                .finally(function() {
                    $rootScope.loadingProcess = false;
                });
        };

        $scope.reloadBlogPosts();
    }]);

