'use strict';

angular.module('flightalertApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('flightRequest', {
                parent: 'entity',
                url: '/flightRequests',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'flightalertApp.flightRequest.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/flightRequest/flightRequests.html',
                        controller: 'FlightRequestController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('flightRequest');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('flightRequest.detail', {
                parent: 'entity',
                url: '/flightRequest/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'flightalertApp.flightRequest.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/flightRequest/flightRequest-detail.html',
                        controller: 'FlightRequestDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('flightRequest');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'FlightRequest', function($stateParams, FlightRequest) {
                        return FlightRequest.get({id : $stateParams.id});
                    }]
                }
            })
            .state('flightRequest.new', {
                parent: 'flightRequest',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/flightRequest/flightRequest-dialog.html',
                        controller: 'FlightRequestDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    validFromDate: null,
                                    validToDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('flightRequest', null, { reload: true });
                    }, function() {
                        $state.go('flightRequest');
                    })
                }]
            })
            .state('flightRequest.edit', {
                parent: 'flightRequest',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/flightRequest/flightRequest-dialog.html',
                        controller: 'FlightRequestDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['FlightRequest', function(FlightRequest) {
                                return FlightRequest.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('flightRequest', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('flightRequest.delete', {
                parent: 'flightRequest',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/flightRequest/flightRequest-delete-dialog.html',
                        controller: 'FlightRequestDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['FlightRequest', function(FlightRequest) {
                                return FlightRequest.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('flightRequest', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
