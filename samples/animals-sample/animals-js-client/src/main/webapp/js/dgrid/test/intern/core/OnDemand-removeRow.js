define([
	"intern!tdd",
	"intern/chai!assert",
	"dojo/_base/array",
	"dojo/_base/lang",
	"dojo/store/Memory",
	"dojo/store/Observable",
	"dojo/query",
	"dgrid/OnDemandList",
	"put-selector/put"
], function(test, assert, arrayUtil, lang, Memory, Observable, query, OnDemandList, put){

	test.suite("removeRow", function(){

		var list;

		function testInitialObservers(list, comment){
			var observers = list.observers;
			arrayUtil.forEach([true, true, true], function(test, i){
				assert.isTrue(!!observers[i] === test, [comment, "index is " + i + ", Expected is " + test]);
			});
		}

		function countObserverReferences(listId, observerIndex){
			var count = 0;
			query(".dgrid-row", listId).forEach(function(row){
				if(row.observerIndex === observerIndex){
					count += 1;
				}
			});
			return count;
		}

		test.beforeEach(function(){
			var data = [];
			for(var i = 0; i < 1000; i++){
				data.push({id: i, value: i});
			}

			var store = Observable(new Memory({ data: data }));
			list = new OnDemandList({
				id: "list1",
				store: store,
				queryRowsOverlap: 0,
				renderRow: function(object){
					return put("div", object.value);
				},
				minRowsPerPage: 10,
				maxRowsPerPage: 10
			});
			put(document.body, list.domNode);
			put(list.domNode, "[style=height:300px]");
			list.startup();
		});

		test.afterEach(function(){
			list.destroy();
		});

		test.test("OnDemandList w/observers - remove all, clean up only", function(){
			var countRefs = lang.partial(countObserverReferences, "list1");
			testInitialObservers(list, "Initial");
			assert.strictEqual(10, countRefs(0));
			assert.strictEqual(10, countRefs(1));
			for(var i = 0; i < 9; i++){
				var rowNode = document.getElementById("list1-row-" + i);
				assert.strictEqual(0, rowNode.observerIndex, "Row's observerIndex");
				assert.strictEqual(10 - i, countRefs(0), "Iteration " + i + ": observer reference count");
				list.removeRow(rowNode, true);
				testInitialObservers(list, "Iteration " + i);
				assert.strictEqual(9 - i, countRefs(0), "Iteration " + i + ": observer reference count");
				assert.strictEqual(10, countRefs(1));
			}

			list.removeRow(document.getElementById("list1-row-9"), true);
			assert.strictEqual(0, countRefs(0));
			assert.strictEqual(10, countRefs(1));
			arrayUtil.forEach([false, true, true], function(test, i){
				var observers = list.observers;
				assert.isTrue(!!observers[i] === test, ["i: " + i + ", test = " + test, observers]);
			});
		});

		test.test("OnDemandList w/observers - remove last 2, clean up only", function(){
			var countRefs = lang.partial(countObserverReferences, "list1");
			// Removing the last two rows from observer #0 should not cancel the observer.
			testInitialObservers(list, "Initial");
			assert.strictEqual(10, countRefs(0));
			assert.strictEqual(10, countRefs(1));

			list.removeRow(document.getElementById("list1-row-7"), true);
			testInitialObservers(list, "Removed row 8");
			assert.strictEqual(9, countRefs(0));
			assert.strictEqual(10, countRefs(1));

			list.removeRow(document.getElementById("list1-row-8"), true);
			testInitialObservers(list, "Removed row 9");
			assert.strictEqual(8, countRefs(0));
			assert.strictEqual(10, countRefs(1));

			list.removeRow(document.getElementById("list1-row-1"), true);
			testInitialObservers(list, "Removed row 1");
			assert.strictEqual(7, countRefs(0));
			assert.strictEqual(10, countRefs(1));

			list.removeRow(document.getElementById("list1-row-0"), true);
			testInitialObservers(list, "Removed row 0");
			assert.strictEqual(6, countRefs(0));
			assert.strictEqual(10, countRefs(1));
		});
	});
});