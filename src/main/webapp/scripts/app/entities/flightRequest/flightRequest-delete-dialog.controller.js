'use strict';

angular.module('flightalertApp')
	.controller('FlightRequestDeleteController', function($scope, $uibModalInstance, entity, FlightRequest) {

        $scope.flightRequest = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            FlightRequest.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
