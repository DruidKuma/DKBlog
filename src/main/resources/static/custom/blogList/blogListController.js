/**
 * Created by DruidKuma on 7/12/16.
 */
angular.module("blogApp")
    .controller('BlogListController',['$scope', '$rootScope', 'BlogEntry', function($scope, $rootScope, BlogEntry) {
        $scope.$on('$routeChangeSuccess', function () {
            $rootScope.pageTitle = "All posts";
        });

        BlogEntry.all().then(function(response) {
            $scope.posts = response.data;
        }, function(error) {
        //    TODO
        });
    }]);