/**
 * Created by DruidKuma on 7/26/16.
 */
angular.module("blogApp")
    .controller('i18nPanelController',['$scope', 'I18NService', function($scope, I18NService) {
        //Page Heading
        $scope.$on('$routeChangeSuccess', function () {
            $scope.pageHeading.title = "I18N Panel";
        });

        $scope.loadTopLevelGroups = function() {
            I18NService.groupNames("").then(function(response) {
                $scope.topLevelGroupNames = response.data;
            });
        };

        $scope.loadTopLevelGroups();
    }]);