'use strict';

angular.module('flightalertApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('flightSlice', {
                parent: 'entity',
                url: '/flightSlices',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'flightalertApp.flightSlice.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/flightSlice/flightSlices.html',
                        controller: 'FlightSliceController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('flightSlice');
                        $translatePartialLoader.addPart('cabinClass');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('flightSlice.detail', {
                parent: 'entity',
                url: '/flightSlice/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'flightalertApp.flightSlice.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/flightSlice/flightSlice-detail.html',
                        controller: 'FlightSliceDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('flightSlice');
                        $translatePartialLoader.addPart('cabinClass');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'FlightSlice', function($stateParams, FlightSlice) {
                        return FlightSlice.get({id : $stateParams.id});
                    }]
                }
            })
            .state('flightSlice.new', {
                parent: 'flightSlice',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/flightSlice/flightSlice-dialog.html',
                        controller: 'FlightSliceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    origin: null,
                                    destination: null,
                                    preferredCabin: null,
                                    date: null,
                                    maxStops: null,
                                    maxConnectionDurationInMinutes: null,
                                    maxPriceInCHF: null,
                                    refundable: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('flightSlice', null, { reload: true });
                    }, function() {
                        $state.go('flightSlice');
                    })
                }]
            })
            .state('flightSlice.edit', {
                parent: 'flightSlice',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/flightSlice/flightSlice-dialog.html',
                        controller: 'FlightSliceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['FlightSlice', function(FlightSlice) {
                                return FlightSlice.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('flightSlice', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('flightSlice.delete', {
                parent: 'flightSlice',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/flightSlice/flightSlice-delete-dialog.html',
                        controller: 'FlightSliceDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['FlightSlice', function(FlightSlice) {
                                return FlightSlice.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('flightSlice', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
