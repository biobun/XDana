package utils;

import org.jdom.Namespace;

/**
 * Created by thinksmart on 9/30/14.
 */
public class Constant {
 public static final String IDENTIFIER_SCHEMA = "www.bapepam.go.id/showcase/alpha";
 public interface NameSpace{
 public static final Namespace ICM_BS_NAMESPACE = Namespace.getNamespace(
 "icm-bs","http://www.bapepam.go.id/id/fr/ifrs/ci/id/2006-07-31");
 public static final Namespace REF_NAMESPACE = Namespace.getNamespace(
 "ref", "http://www.xbrl.org/2004/ref");
 public static final Namespace ICM_BS_A_NAMESPACE = Namespace.getNamespace(
 "icm-bs-a","http://www.bapepam.go.id/id/fr/ifrs/ci/id/2006-07-31/audited");
 }
}