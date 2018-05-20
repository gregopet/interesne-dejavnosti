package si.francebevk.db

import org.jooq.DSLContext
import org.jooq.impl.DSL

/** Run [block] in a transaction and return its result */
inline fun <T> DSLContext.withTransaction(crossinline block: (ctx: DSLContext) -> T) : T =
        this.transactionResult { cfg -> block.invoke(DSL.using(cfg)) }