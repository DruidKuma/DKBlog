var blogApp = angular.module("blogApp", ['ngRoute', 'ui.bootstrap']);
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

    when('/entry/:id', {
        templateUrl: '/custom/blogEntry/blogEntry.html',
        controller: 'BlogEntryController'
    }).

    otherwise({
        redirectTo: '/'
    });
}]);

blogApp.filter('cut', function () {
    return function (value, wordwise, max, tail) {
        if (!value) return '';

        max = parseInt(max, 10);
        if (!max) return value;
        if (value.length <= max) return value;

        value = value.substr(0, max);
        if (wordwise) {
            var lastspace = value.lastIndexOf(' ');
            if (lastspace != -1) {
                //Also remove . and , so its gives a cleaner result.
                if (value.charAt(lastspace-1) == '.' || value.charAt(lastspace-1) == ',') {
                    lastspace = lastspace - 1;
                }
                value = value.substr(0, lastspace);
            }
        }

        return value + (tail || ' …');
    };
});