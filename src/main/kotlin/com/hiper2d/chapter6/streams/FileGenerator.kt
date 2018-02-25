package com.hiper2d.chapter6.streams

fun generateFile(size: Int) =
    (1 until size).map {
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi lobortis cursus venenatis. " +
                "Mauris tempus elit ut malesuada luctus. Interdum et malesuada fames ac ante ipsum primis in faucibus. " +
                "Phasellus laoreet sapien eu pulvinar rhoncus. Integer vel ultricies leo. Donec vel sagittis nibh. " +
                "Maecenas eu quam non est hendrerit pu"
    }.toList()