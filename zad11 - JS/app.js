"use strict";

const API_URL = 'http://localhost:9999/';


var Model = function (path) {

    var self = this;
    var getPath = function (query) {
        var parentId = self.isGrade ? viewController.currentStudent : null;
        var temp = API_URL + self.path.split('/').join('/' + parentId + '/') + (!query ? '/' : query);
        console.log(temp);
        return temp;
    };

    self.path = path;
    self.isGrade = (path === 'students/grades');
    self.objects = ko.observableArray();
    self.idFieldName = (path === 'students') ? 'index' : 'id';

    self.get = function (query) {
        console.log('GET');
        return $.ajax({
            url: getPath(query),
            type: "GET",
            accept: 'application/json; charset=utf-8'
        });
    };

    self.post = function (object) {
        console.log('POST');
        return $.ajax({
            url: getPath(),
            type: "POST",
            data: ko.mapping.toJSON(object),
            contentType: "application/json; charset=utf-8",
        })
    };

    self.put = function (object) {
        console.log('PUT');
        return $.ajax({
            url: getPath() + object[self.idFieldName](),
            type: "PUT",
            data: ko.mapping.toJSON(object),
            contentType: "application/json; charset=utf-8",
        })
    };

    self.delete = function (object) {
        console.log('DELETE');
        return $.ajax({
            url: getPath() + object[self.idFieldName](),
            type: "DELETE",
        })
    };

};

var getAllData = function(viewController) {
    viewController.get(viewController.subjects);
    viewController.get(viewController.students);
};

var Student = function() { return {index: ko.observable(), name: ko.observable(), surname: ko.observable(), birthDate: ko.observable()}};
var Grade = function() { return {id: null, value: ko.observable(), subject: ko.observable(), date: ko.observable()}};
var Subject = function() { return {id: null, professor: ko.observable(), name: ko.observable()}};

var ViewController = function() {

    var self = this;
    self.students = new Model('students');
    self.subjects = new Model('subjects');
    self.grades = new Model('students/grades');

    self.newStudent = Student();
    self.newGrade = Grade();
    self.newSubject = Subject();

    self.currentStudent = 0;

    self.addObject = function(objectName, group) {
        group.post(self[objectName]).then(function () {
            var queryField = self.filters[objectName === 'newStudent' ? 'students' : objectName === 'newSubject' ? 'subjects' : 'grades'];
            self.get(group, self.getQuery(queryField));
        })
    };

    var dataToObservable = function (data) {
        var observables = ko.observableArray();
        data.forEach(function(object) {
            var newObject = {};
            for (var field in object) {
                if (field !== 'link' && object.hasOwnProperty(field)) {
                    newObject[field] = ko.observable(object[field]);
                }
            }
            observables.push(newObject);
        });
        return observables;
    };

    self.get = function(group, query) {
        group.get(query).then(function (data) {
            var observables = dataToObservable(data);
            group.objects.removeAll();
            observables().forEach(function(o) {
                group.objects.push(o);
            });
            observables().forEach(function (object) {
                for (var field in object) {
                    if (object.hasOwnProperty(field) && typeof object[field] === "function") {
                        object[field].subscribe(function (changes) {
                            group.put(object);
                        }, null, 'change')
                    }
                }
            });
            // Post i delete w reakcjach na arrayChange
            //
            // group.objects.subscribe(function (changes) {
            //     changes.forEach(function (change) {
            //         console.log(change);
            //         if (change.status === 'added' && change.value) {
            //             group.post(change.value).catch(function () {
            //             });
            //         } else if (change.status === 'deleted' && !change.value[self.idFieldName]) {
            //             group.delete(change.value)
            //         }
            //     })
            // }, null, 'arrayChange');
        });
    };

    self.delete = function(object, group) {
        group.delete(object).then(function () {
            group.objects.remove(object);
        });
    };

    self.onGoToGrades = function(index) {
        self.currentStudent = index;
        self.get(self.grades);
        return true;
    };

    self.getQuery = function(field) {
        var query = '?';
        for (var f in field) {
            if (field.hasOwnProperty(f) && field[f]()) {
                query += f + '=' + field[f]() + '&';
            }
        }
        return query;
    };

    self.filters = {
        students: {
            index: ko.observable(),
            name: ko.observable(),
            surname: ko.observable(),
            dateFrom: ko.observable()
        },
        subjects: {
            professor: ko.observable(),
            name: ko.observable()
        },
        grades: {
            date: ko.observable(),
            minValue: ko.observable(),
            subject: ko.observable()
        }
    };
    for (const categoryName in self.filters) {
        if (self.filters.hasOwnProperty(categoryName)) {
            const category = self.filters[categoryName];
            for (var field in category) {
                if (category.hasOwnProperty(field)) {
                    category[field].subscribe(function() {
                        self.get(self[categoryName], self.getQuery(category));
                    }, null, 'change')
                }
            }
        }
    }
};



var viewController = new ViewController();
getAllData(viewController);

$(function () {
    setTimeout(function() {
        ko.applyBindings(viewController);
    }, 500);
});