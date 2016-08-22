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

        $scope.currentGroupView = {
            groups: ['links', 'labels', 'siska', 'sosiska'],
            translations: {
                firstTranslation: {
                    src: 'Source',
                    target: 'Target Translation'
                },
                secondTranslation: {
                    src: 'Siska',
                    target: 'Sosiska'
                },
                thirdTranslation: {
                    src: 'Piska',
                    target: 'Pipiska'
                },
                fourthTranslation: {
                    src: 'Java',
                    target: 'Python'
                },
                fifthTranslation: {
                    src: 'Light',
                    target: 'Dark SIDE...'
                }
            }
        };

        $scope.changeTargetCountry = function(country) {
            $scope.targetCountry = country;
        };

        $scope.resetFilter = function() {
            $scope.chosenGroupParts = [];
        };

        $scope.removeGroupParts = function(index) {
            $scope.chosenGroupParts.splice(index+1, $scope.chosenGroupParts.length);
        };

    }]);