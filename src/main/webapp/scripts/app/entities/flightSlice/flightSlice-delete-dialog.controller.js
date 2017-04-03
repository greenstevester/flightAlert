'use strict';

angular.module('flightalertApp')
	.controller('FlightSliceDeleteController', function($scope, $uibModalInstance, entity, FlightSlice) {

        $scope.flightSlice = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            FlightSlice.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
