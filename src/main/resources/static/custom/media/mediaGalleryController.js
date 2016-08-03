/**
 * Created by DruidKuma on 8/2/16.
 */
angular.module("blogApp").controller('MediaGalleryController',['$scope', 'Media', function($scope, Media) {
    //Page Heading
    $scope.$on('$routeChangeSuccess', function () {
        $scope.pageHeading.title = "Media Gallery";
    });
}]);