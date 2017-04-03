'use strict';

angular.module('flightalertApp')
	.controller('PermittedCarrierDeleteController', function($scope, $uibModalInstance, entity, PermittedCarrier) {

        $scope.permittedCarrier = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            PermittedCarrier.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
