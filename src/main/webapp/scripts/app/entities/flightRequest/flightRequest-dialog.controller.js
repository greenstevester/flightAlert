'use strict';

angular.module('flightalertApp').controller('FlightRequestDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'FlightRequest', 'FlightSlice',
        function($scope, $stateParams, $uibModalInstance, entity, FlightRequest, FlightSlice) {

        $scope.flightRequest = entity;
        $scope.flightslices = FlightSlice.query();
        $scope.load = function(id) {
            FlightRequest.get({id : id}, function(result) {
                $scope.flightRequest = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('flightalertApp:flightRequestUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.flightRequest.id != null) {
                FlightRequest.update($scope.flightRequest, onSaveSuccess, onSaveError);
            } else {
                FlightRequest.save($scope.flightRequest, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForValidFromDate = {};

        $scope.datePickerForValidFromDate.status = {
            opened: false
        };

        $scope.datePickerForValidFromDateOpen = function($event) {
            $scope.datePickerForValidFromDate.status.opened = true;
        };
        $scope.datePickerForValidToDate = {};

        $scope.datePickerForValidToDate.status = {
            opened: false
        };

        $scope.datePickerForValidToDateOpen = function($event) {
            $scope.datePickerForValidToDate.status.opened = true;
        };
}]);
