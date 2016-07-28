/**
 * Created by DruidKuma on 7/12/16.
 */
angular.module("blogApp")
    .controller('CategoryController',['$scope', 'Category', function($scope, Category) {
        //Page Heading
        $scope.$on('$routeChangeSuccess', function () {
            $scope.pageHeading.title = "Category Overview";
        });

        $scope.loadCategories = function() {
            Category.all().then(function(response) {
                $scope.categories = response.data;
            });
        }
    }]);

