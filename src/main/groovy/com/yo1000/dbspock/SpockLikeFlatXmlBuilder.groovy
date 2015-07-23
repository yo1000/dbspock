package com.yo1000.dbspock
/**
 * Created by yoichi.kikuchi on 2015/07/13.
 */
class SpockLikeFlatXmlBuilder extends GroovyObjectSupport {
    def cols = []
    def items = []

    SpockLikeFlatXmlBuilder() {
        Object.metaClass.or = { x ->
            if (!(delegate instanceof List)) {
                return [delegate, x]
            }
            delegate << x
        }
    }

    @Override
    Object invokeMethod(String name, Object args) {
        if (!(args instanceof Object[]) || args.size() <= 0) {
            return super.invokeMethod(name, args)
        }

        def arg = args[0]

        if (name.toLowerCase() == "_cols_") {
            cols = arg
            return
        }

        def builder = new StringBuilder(name)

        for (def i = 0; i < cols.size(); i++) {
            if (arg[i] == null) continue
            builder.append(/ ${cols[i]}="${arg[i]}"/)
        }

        this.items << "<${builder.toString()}/>"
    }

    String build() {
        def builder = new StringBuilder("<dataset>")
        for (def item : this.items) {
            builder.append(item)
        }
        builder.append("</dataset>")

        return builder.toString()
    }
}
