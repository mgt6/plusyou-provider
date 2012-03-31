package com.openplanetideas.plusyou.provider.external;

/**
 * This interface is implemented by any class which is generated
 * externally to allow import into our system.
 *
 * @author Mark Taylor <marktaycon@googlemail.com>
 * @version 1.6
 * @since 30/03/12
 */
public interface ExternalMapper<EXTERNAL, INTERNAL> {

    /**
     * Creates an internal domain object from an external source
     * @param external the external data object
     * @return internal domain object
     */
    public INTERNAL fromExternal(EXTERNAL external);

}
