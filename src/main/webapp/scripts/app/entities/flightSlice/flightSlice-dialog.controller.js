'use strict';

angular.module('flightalertApp').controller('FlightSliceDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'FlightSlice', 'PermittedCarrier', 'Passenger', 'FlightRequest',
        function($scope, $stateParams, $uibModalInstance, entity, FlightSlice, PermittedCarrier, Passenger, FlightRequest) {

        $scope.flightSlice = entity;
        $scope.permittedcarriers = PermittedCarrier.query();
        $scope.passengers = Passenger.query();
        $scope.flightrequests = FlightRequest.query();
        $scope.load = function(id) {
            FlightSlice.get({id : id}, function(result) {
                $scope.flightSlice = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('flightalertApp:flightSliceUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.flightSlice.id != null) {
                FlightSlice.update($scope.flightSlice, onSaveSuccess, onSaveError);
            } else {
                FlightSlice.save($scope.flightSlice, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForDate = {};

        $scope.datePickerForDate.status = {
            opened: false
        };

        $scope.datePickerForDateOpen = function($event) {
            $scope.datePickerForDate.status.opened = true;
        };
}]);
