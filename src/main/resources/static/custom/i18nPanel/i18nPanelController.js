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
            translations: [
                {
                    key: 'firstTranslation',
                    src: 'Source',
                    target: 'Target Translation',
                    lastModified: new Date()
                },
                {
                    key: 'secondTranslation',
                    src: 'Siska',
                    target: 'Sosiska',
                    lastModified: new Date()
                },
                {
                    key: 'thirdTranslation',
                    src: 'Piska',
                    target: 'Pipiska',
                    lastModified: new Date()
                },
                {
                    key: 'fourthTranslation',
                    src: 'Java',
                    target: 'Python',
                    lastModified: new Date()
                },{
                    key: 'fifthTranslation',
                    src: 'Light',
                    target: 'Dark SIDE...',
                    lastModified: new Date()
                }
            ]
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

        $scope.openGroup = function(group) {
            $scope.chosenGroupParts.push(group);
        };

        $scope.saveTranslation = function(data, translation) {

        };

        $scope.removeTranslation = function(translation) {
            $scope.executeWithWarning("Are you sure?",
                "This action will delete this translation key with all translations and can influence the work of the whole application!",
                "Yes, delete it!",
                function() {
                    console.log('Implement me');
                });
        };

        $scope.addNewTranslation = function() {
            $scope.inserted = {
                key: '',
                src: '',
                target: '',
                lastModified: new Date(),
                new: true
            };
            $scope.currentGroupView.translations.push($scope.inserted);
        };

        $scope.cancelFormEditing = function(form) {
            form.$cancel();
            if($scope.inserted) {
                $scope.currentGroupView.translations.pop();
            }
            delete $scope.inserted;
        };

    }]);