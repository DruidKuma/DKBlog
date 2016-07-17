/**
 * Created by DruidKuma on 7/12/16.
 */
angular.module("blogApp")
    .controller('HomeController', function($scope, $rootScope) {
        $scope.$on('$routeChangeSuccess', function () {
            $rootScope.pageTitle = "Dashboard";
            $.Dashboard1.init();
            $.Components.init();
            $.App.init();
        });
    });