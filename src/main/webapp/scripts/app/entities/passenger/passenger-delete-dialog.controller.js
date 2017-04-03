'use strict';

angular.module('flightalertApp')
	.controller('PassengerDeleteController', function($scope, $uibModalInstance, entity, Passenger) {

        $scope.passenger = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Passenger.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
