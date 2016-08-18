angular.module('uiSwitch', [])

    .directive('switch', function () {
        return {
            restrict: 'AE'
            , replace: true
            , transclude: true
            , template: function (element, attrs) {
                var html = '';
                html += '<span';
                html += ' class="switch' + (attrs.class ? ' ' + attrs.class : '') + '"';
                html += ' ng-click="' + attrs.ngModel + ' = ' + "'" + attrs.value + (attrs.ngChange ? '\'; ' + attrs.ngChange + '"' : '\'"');
                html += ' ng-class="{ checked:' + attrs.ngModel + ' == \'' + attrs.value + '\', disabled:' + attrs.disabled + ' }"';
                html += '>';
                html += '<small></small>';
                html += '<input type="radio"';
                html += attrs.value ? ' value="' + attrs.value + '"' : '';
                html += attrs.id ? ' id="' + attrs.id + '"' : '';
                html += attrs.name ? ' name="' + attrs.name + '"' : '';
                html += attrs.ngModel ? ' ng-model="' + attrs.ngModel + '"' : '';
                html += ' style="display:none" />';
                html += '<span class="switch-text">';
                /*adding new container for switch text*/
                html += attrs.on ? '<span class="on">' + attrs.on + '</span>' : '';
                /*switch text on value set by user in directive html markup*/
                html += attrs.off ? '<span class="off">' + attrs.off + '</span>' : ' ';
                /*switch text off value set by user in directive html markup*/
                html += '</span>';
                return html;
            }
        }
    });
