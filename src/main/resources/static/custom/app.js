var blogApp = angular.module("blogApp", ['ngRoute']);
var BASE_URL = 'http://localhost:8081';

blogApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider.

    when('/', {
        templateUrl: '/custom/home/home.html',
        controller: 'HomeController'
    }).

    when('/list', {
        templateUrl: '/custom/blogList/blogList.html',
        controller: 'BlogListController'
    }).

    otherwise({
        redirectTo: '/'
    });

}]);