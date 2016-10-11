(function() {
    'use strict';

    angular
        .module('attendanceApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('attendee', {
            parent: 'entity',
            url: '/attendee',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Attendees'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/attendee/attendees.html',
                    controller: 'AttendeeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('attendee-detail', {
            parent: 'entity',
            url: '/attendee/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Attendee'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/attendee/attendee-detail.html',
                    controller: 'AttendeeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Attendee', function($stateParams, Attendee) {
                    return Attendee.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'attendee',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('attendee-detail.edit', {
            parent: 'attendee-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/attendee/attendee-dialog.html',
                    controller: 'AttendeeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Attendee', function(Attendee) {
                            return Attendee.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('attendee.new', {
            parent: 'attendee',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/attendee/attendee-dialog.html',
                    controller: 'AttendeeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                firstName: null,
                                lastName: null,
                                email: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('attendee', null, { reload: 'attendee' });
                }, function() {
                    $state.go('attendee');
                });
            }]
        })
        .state('attendee.edit', {
            parent: 'attendee',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/attendee/attendee-dialog.html',
                    controller: 'AttendeeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Attendee', function(Attendee) {
                            return Attendee.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('attendee', null, { reload: 'attendee' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('attendee.delete', {
            parent: 'attendee',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/attendee/attendee-delete-dialog.html',
                    controller: 'AttendeeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Attendee', function(Attendee) {
                            return Attendee.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('attendee', null, { reload: 'attendee' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
