package at.irian.ankor.core.ref;

import at.irian.ankor.core.application.ModelHolder;
import at.irian.ankor.core.el.StandardELContext;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.el.ExpressionFactory;

/**
 * @author MGeiler (Manfred Geiler)
 */
public class RefTest {
    //private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(RefTest.class);

    private ModelHolder modelHolder;
    private RefFactory refFactory;

    @Before
    public void setup() {
        this.modelHolder = new ModelHolder(Object.class);
        this.refFactory = new RefFactory(ExpressionFactory.newInstance(),
                                         new StandardELContext(),
                                         null,
                                         null,
                                         modelHolder);
    }

    @Test
    public void test_root_path() throws Exception {
        Ref ref = refFactory.rootRef();
        Assert.assertEquals("model", ref.path());
    }

    @Test
    public void test_prop_path() throws Exception {
        Ref ref = refFactory.ref("model.test.person.name");
        Assert.assertEquals("model.test.person.name", ref.path());
    }

    @Test
    public void test_prop_path_auto() throws Exception {
        Ref ref = refFactory.ref("test.person.name");
        Assert.assertEquals("model.test.person.name", ref.path());
    }

    @Test
    public void test_parent_path() throws Exception {
        Ref ref = refFactory.ref("model.test.person.name");
        Assert.assertEquals("model.test.person", ref.parent().path());
    }

    @Test
    public void test_descendant() throws Exception {
        Ref ref = refFactory.ref("model.test.person.name");
        Assert.assertTrue(ref.isDescendantOf(refFactory.rootRef()));
        Assert.assertTrue(ref.isDescendantOf(refFactory.ref("model.test.person")));
        Assert.assertTrue(ref.isDescendantOf(refFactory.ref("model.test")));
        Assert.assertTrue(ref.isDescendantOf(refFactory.ref("model")));
        Assert.assertTrue(ref.isDescendantOf(refFactory.ref("")));
        Assert.assertFalse(ref.isDescendantOf(refFactory.ref("model.foo.bar")));
        Assert.assertFalse(ref.isDescendantOf(refFactory.ref("model.foo")));
    }
}
