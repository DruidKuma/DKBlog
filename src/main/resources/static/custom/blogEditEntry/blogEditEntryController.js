/**
 * Created by DruidKuma on 7/21/16.
 */
angular.module("blogApp")
    .controller('BlogEditEntryController',['$scope', 'BlogEntry', '$routeParams', '$sce', '$location', 'Country', function($scope, BlogEntry, $routeParams, $sce, $location, Country) {

        $scope.loadBlogPost = function() {
            if($routeParams.id) {
                $scope.loadingProcess = true;
                BlogEntry.single($routeParams.id).then(function(response) {
                    $scope.postEntry = response.data;
                    $scope.pageHeading.title = "Blog Entry Editor";
                    $scope.editEntryModel = $scope.postEntry.content;

                }).finally(function() {
                    $scope.loadingProcess = false;
                });
            }
        };

        $scope.chosenCountries = [
            { name: "America", isoCode: "US" },
            { name: "Spain", isoCode: "ES" },
            { name: "Germany", isoCode: "DE" },
            { name: "Ukraine", isoCode: "UA" },
        ];


        $scope.loadCountries = function($query) {
            return Country.flags().then(function(response) {
                var countries = response.data;
                return countries.filter(function(country) {
                    return country.name.toLowerCase().indexOf($query.toLowerCase()) != -1;
                });
            });
        };



        $scope.loadBlogPost();
    }]);