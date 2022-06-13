#!/usr/bin/env nextflow
nextflow.enable.dsl=2

params.inputdata = 'scripts'

process executeSimulations {

    input:
        path x

    output:
        path 'foo.txt'

    script:
      """
      tail ${x}
      touch foo.txt
      cat foo.txt
      """
}

workflow {
    //data = Channel.fromPath( "${params.inputdata}/*", type: 'dir') .flatten()
    data = channel.fromPath('scripts/*.sh')
    executeSimulations(data)
}
