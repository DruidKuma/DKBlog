/**
 * Created by DruidKuma on 7/12/16.
 */
angular.module("blogApp")
    .controller('BlogListController',['$scope', '$rootScope', 'BlogEntry', function($scope, $rootScope, BlogEntry) {

        $scope.totalItems = 2050;
        $scope.currentPage = 8;
        $scope.numPageSize = 5;

        $scope.pageChanged = function() {
            console.log("Page changed to: " + $scope.currentPage);
        };

        $scope.$on('$routeChangeSuccess', function () {
            $rootScope.pageTitle = "All posts";
        });

        BlogEntry.all().then(function(response) {
            $scope.posts = response.data.content;
        }, function(error) {
        //    TODO
        });
    }]);