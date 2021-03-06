{expect} = require("chai")
ankor = require("./testmodel")
{ModelType} = require("../lib/model/Type")
{Model} = require("../lib/model/Model")
{Context} = require("../lib/model/Context")

describe("Ankor", ->
    context = null
    beforeEach((done) ->
        ankor.instantiateContext((err, createdContext) ->
            context = createdContext
            done(err)
        )
    )
    describe("#instantiateContext", ->
        it("should return a Context object with a id, session and model", ->
            expect(context).to.be.an.instanceof(Context)
            expect(context.id).to.be.a("string")
            expect(context.id).to.have.length(36)
            expect(context.model).to.be.an.instanceof(Model)
            expect(context.session).to.be.an("object")
        )
    )
    describe("#instantiateModel", ->
        it("should return a Model object with the given type", ->
            animal = ankor.instantiateModel("Animal")
            expect(animal).to.be.an.instanceof(Model)
            expect(animal.type == ankor.TYPE("Animal")).to.be.true
        )
    )
)