/**
 * Created by DruidKuma on 7/26/16.
 */
angular.module("blogApp")
    .controller('I18nPanelController',['$scope', 'I18NService', function($scope, I18NService) {
        //Page Heading
        $scope.$on('$routeChangeSuccess', function () {
            $scope.pageHeading.title = "I18N Panel";
        });
        $scope.targetCountry = $scope.currentCountry;


        $scope.chosenGroupParts = ['layout', 'sidebar', 'menu'];

        $scope.changeTargetCountry = function(country) {
            $scope.targetCountry = country;
        };

        $scope.resetFilter = function() {
            $scope.chosenGroupParts = [];
        }

    }]);