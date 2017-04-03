'use strict';

angular.module('flightalertApp').controller('PermittedCarrierDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'PermittedCarrier', 'FlightSlice',
        function($scope, $stateParams, $uibModalInstance, entity, PermittedCarrier, FlightSlice) {

        $scope.permittedCarrier = entity;
        $scope.flightslices = FlightSlice.query();
        $scope.load = function(id) {
            PermittedCarrier.get({id : id}, function(result) {
                $scope.permittedCarrier = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('flightalertApp:permittedCarrierUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.permittedCarrier.id != null) {
                PermittedCarrier.update($scope.permittedCarrier, onSaveSuccess, onSaveError);
            } else {
                PermittedCarrier.save($scope.permittedCarrier, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
