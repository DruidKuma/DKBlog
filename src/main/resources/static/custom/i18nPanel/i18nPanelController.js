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

        $scope.loadGroup = function(group) {
            //console.log(group);
        };

        $scope.topLevelGroups = [
            {
                name: 'layout',
                translations: [
                    {key: 'domain', valueFrom: 'Tyres', valueTo: 'Reifen'},
                    {key: 'extension', valueFrom: 'NET', valueTo: 'DE'}
                ],
                children: [
                    {
                        name: 'menu',
                        translations: [
                            {key: 'dashboard', valueFrom: 'Dashboard', valueTo: 'Kugelschreiber'},
                            {key: 'publishedPosts', valueFrom: 'Published Posts', valueTo: 'Schmeter Linger'}
                        ],
                        children: []
                    },
                    {
                        name: 'titles',
                        translations: [
                            {key: 'listTitle', valueFrom: 'All Posts', valueTo: 'Alle Posten'},
                            {key: 'i18nTitle', valueFrom: 'I18N Panel', valueTo: 'I18N Panelle'}
                        ],
                        children: []
                    }
                ]
            },
            {
                name: 'postList',
                translations: [
                    {key: 'siska', valueFrom: 'Sosiska', valueTo: 'Sosiso4ka'},
                    {key: 'piska', valueFrom: 'Pipiska', valueTo: 'Pipiso4ka'}
                ],
                children: []
            }
        ];

        //$scope.loadTopLevelGroups();
    }]);