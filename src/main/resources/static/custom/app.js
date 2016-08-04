var blogApp = angular.module("blogApp", ['ngRoute', 'ui.bootstrap', 'ui.tinymce', 'ngCookies', 'pascalprecht.translate', 'ngTagsInput', 'truncate', 'color.picker', 'ngFileUpload']);
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
        controller: 'I18nPanelController'
    }).

    when('/category', {
        templateUrl: '/custom/category/categoryList.html',
        controller: 'CategoryController'
    }).

    when('/media', {
        templateUrl: '/custom/media/mediaGallery.html',
        controller: 'MediaGalleryController'
    }).

    otherwise({
        redirectTo: '/'
    });


    $translateProvider.useLoader('$translatePartialLoader', {
        urlTemplate: '/api/blog/i18n/translate/{part}/{lang}'
    });
    $translateProvider.preferredLanguage('en');
    $translateProvider.fallbackLanguage('en');
    $translateProvider.useCookieStorage();

    $translatePartialLoaderProvider.addPart('layout');
}]);