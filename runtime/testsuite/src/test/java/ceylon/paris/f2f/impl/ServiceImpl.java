

package ceylon.paris.f2f.impl;

import ceylon.paris.f2f.iface.IService;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ServiceImpl implements IService {
    public void poke() {
        System.out.println("Poke!!");
    }
}
