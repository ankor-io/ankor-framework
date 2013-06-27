package at.irian.ankor.path.el;

import at.irian.ankor.path.PathSyntax;

/**
 * @author Manfred Geiler
 */
public class ELPathSyntax implements PathSyntax {
    //private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ELPathSyntax.class);

    private static final ELPathSyntax INSTANCE = new ELPathSyntax();

    protected ELPathSyntax() {}

    public static ELPathSyntax getInstance() {
        return INSTANCE;
    }

    @Override
    public String parentOf(String path) {
        if (path.endsWith("]")) {
            int i = path.lastIndexOf('[');
            if (i >= 0) {
                return path.substring(0, i);
            }
        } else {
            int i = path.lastIndexOf('.');
            if (i >= 0) {
                return path.substring(0, i);
            }
        }
        throw new IllegalArgumentException("Not a valid path: " + path);
    }

    boolean isHasParent(String path) {
        return path.indexOf('.') >= 0 || path.indexOf('[') >= 0;
    }

    @Override
    public String concat(String path, String subPath) {
        return path + '.' + subPath;
    }

    @Override
    public String addArrayIdx(String path, int index) {
        return path + '[' + index + ']';
    }

    @Override
    public String addLiteralMapKey(String path, String literalKey) {
        return path + "['" + literalKey + "']";
    }

    @Override
    public String addMapKey(String path, String literalKey) {
        return path + '[' + literalKey + ']';
    }

    @Override
    public boolean isParentChild(String parent, String child) {
        return isHasParent(child) && parentOf(child).equals(parent);
    }

    @Override
    public boolean isDescendant(String descendant, String ancestor) {
        return isParentChild(ancestor, descendant)
               || isHasParent(descendant) && isDescendant(parentOf(descendant), ancestor);
    }

    @Override
    public String getPropertyName(String path) {
        if (path.endsWith("]")) {
            int i = path.lastIndexOf('[');
            return path.substring(i, path.length() - 1);
        } else {
            int i = path.lastIndexOf('.');
            if (i > 0) {
                return path.substring(i + 1);
            } else {
                throw new IllegalArgumentException("Not a valid path: " + path);
            }
        }
    }
}
