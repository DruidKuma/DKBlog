/**
 * Created by DruidKuma on 7/12/16.
 */
angular.module("blogApp").directive('leftSidebar', function() {
    return {
        restrict: 'E',
        replace: true,
        templateUrl: '/custom/sidebar/left-sidebar.html',
        controller: ['$scope', '$location', '$rootScope', 'Country', '$translate', '$cookies', function($scope, $location, $rootScope, Country, $translate, $cookies) {
            $scope.currentState = $location.path();
            $.Sidemenu.init();

            $rootScope.$on('$routeChangeSuccess', function(e, current, pre) {
                $scope.currentState = $location.path();
            });

            $scope.initCountries = function() {
                Country.flags().then(function(response) {
                    $scope.availableCountries = response.data;

                    var selectedCountry = $scope.availableCountries.filter(function(country) {
                        return country.isoCode == $scope.currentCountry.isoCode;
                    })[0];
                    $scope.availableCountries.splice($scope.availableCountries.indexOf(selectedCountry), 1);

                    angular.merge($scope.currentCountry, selectedCountry);
                });
            };

            $scope.changeLanguage = function (country) {
                $scope.currentCountry = country;
                $cookies.put('currentCountryIso', country.isoCode);
                $translate.use(country.defaultLanguageIso);
                $scope.initCountries();

                $rootScope.$broadcast('countryChanged', '');
            };

            $scope.initCountries();
        }]
    }
});