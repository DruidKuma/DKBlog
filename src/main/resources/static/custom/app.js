var blogApp = angular.module("blogApp", ['ngRoute', 'ui.bootstrap', 'ui.tinymce', 'ngCookies', 'pascalprecht.translate', 'ngTagsInput']);
var BASE_URL = 'http://localhost:8081';

blogApp.config(['$routeProvider', '$translateProvider', '$translatePartialLoaderProvider', function($routeProvider, $translateProvider, $translatePartialLoaderProvider) {
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

    when('/entry/edit/:id', {
        templateUrl: '/custom/blogEditEntry/blogEditEntry.html',
        controller: 'BlogEditEntryController'
    }).
        
    when('/i18n', {
        templateUrl: '/custom/i18nPanel/i18nPanel.html',
        controller: 'i18nPanelController'
    }).

    otherwise({
        redirectTo: '/'
    });


    $translateProvider.useLoader('$translatePartialLoader', {
        urlTemplate: '/api/blog/i18n/{part}/{lang}'
    });
    $translateProvider.preferredLanguage('en');
    $translateProvider.fallbackLanguage('en');
    $translateProvider.useCookieStorage();

    $translatePartialLoaderProvider.addPart('layout');
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