/**
 * Created by DruidKuma on 7/12/16.
 */
angular.module("blogApp").directive('leftSidebar', function() {
    return {
        restrict: 'E',
        replace: true,
        templateUrl: '/custom/sidebar/left-sidebar.html',
        controller: ['$scope', '$location', '$rootScope', 'Country', '$translate', function($scope, $location, $rootScope, Country, $translate) {
            $scope.currentState = $location.path();
            $.Sidemenu.init();

            $rootScope.$on('$routeChangeSuccess', function(e, current, pre) {
                $scope.currentState = $location.path();
            });

            $scope.initCountries = function() {
                Country.flags().then(function(response) {
                    $scope.availableCountries = response.data;
                    angular.merge($scope.currentCountry, $scope.availableCountries.filter(function(country) {
                        return country.isoCode == $scope.currentCountry.isoCode;
                    })[0]);
                });
            };

            $scope.changeLanguage = function (langKey) {
                $translate.use(langKey);
                console.log($translate.proposedLanguage());
            };

            $scope.initCountries();
        }]
    }
});