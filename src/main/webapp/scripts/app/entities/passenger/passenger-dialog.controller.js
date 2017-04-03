'use strict';

angular.module('flightalertApp').controller('PassengerDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Passenger', 'FlightSlice',
        function($scope, $stateParams, $uibModalInstance, entity, Passenger, FlightSlice) {

        $scope.passenger = entity;
        $scope.flightslices = FlightSlice.query();
        $scope.load = function(id) {
            Passenger.get({id : id}, function(result) {
                $scope.passenger = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('flightalertApp:passengerUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.passenger.id != null) {
                Passenger.update($scope.passenger, onSaveSuccess, onSaveError);
            } else {
                Passenger.save($scope.passenger, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
