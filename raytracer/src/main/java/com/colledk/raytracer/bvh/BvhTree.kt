package com.colledk.raytracer.bvh

class BvhTree(root: BvhNode) {
    var root: BvhNode
        private set

    init {
        this.root = root
        splitNodes()
    }

    /**
     * Function to create a boundary volume hierarchy based on a given object in 3D space.
     */
    private fun splitNodes() {

    }
}