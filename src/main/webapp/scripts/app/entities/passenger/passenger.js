'use strict';

angular.module('flightalertApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('passenger', {
                parent: 'entity',
                url: '/passengers',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'flightalertApp.passenger.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/passenger/passengers.html',
                        controller: 'PassengerController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('passenger');
                        $translatePartialLoader.addPart('passengerType');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('passenger.detail', {
                parent: 'entity',
                url: '/passenger/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'flightalertApp.passenger.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/passenger/passenger-detail.html',
                        controller: 'PassengerDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('passenger');
                        $translatePartialLoader.addPart('passengerType');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Passenger', function($stateParams, Passenger) {
                        return Passenger.get({id : $stateParams.id});
                    }]
                }
            })
            .state('passenger.new', {
                parent: 'passenger',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/passenger/passenger-dialog.html',
                        controller: 'PassengerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    passengerType: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('passenger', null, { reload: true });
                    }, function() {
                        $state.go('passenger');
                    })
                }]
            })
            .state('passenger.edit', {
                parent: 'passenger',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/passenger/passenger-dialog.html',
                        controller: 'PassengerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Passenger', function(Passenger) {
                                return Passenger.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('passenger', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('passenger.delete', {
                parent: 'passenger',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/passenger/passenger-delete-dialog.html',
                        controller: 'PassengerDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Passenger', function(Passenger) {
                                return Passenger.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('passenger', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
