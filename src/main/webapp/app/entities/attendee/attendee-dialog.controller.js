(function() {
    'use strict';

    angular
        .module('attendanceApp')
        .controller('AttendeeDialogController', AttendeeDialogController);

    AttendeeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Attendee'];

    function AttendeeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Attendee) {
        var vm = this;

        vm.attendee = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.attendee.id !== null) {
                Attendee.update(vm.attendee, onSaveSuccess, onSaveError);
            } else {
                Attendee.save(vm.attendee, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('attendanceApp:attendeeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
