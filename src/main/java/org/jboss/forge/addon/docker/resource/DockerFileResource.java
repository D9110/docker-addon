package org.jboss.forge.addon.docker.resource;

import org.jboss.forge.addon.docker.linter.DockerfileLintResult;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.shrinkwrap.descriptor.api.docker.DockerDescriptor;

/**
 * A {@link Resource} that represents a Dockerfile.
 * 
 * @author <a href="mailto:devanshu911@gmail.com">Devanshu Singh</a>
 */

public interface DockerFileResource extends FileResource<DockerFileResource>
{
   /**
    * Return the {@link DockerfileLintResult} ,lint the underlying Dockerfile against a set of preset base rules.
    * 
    * @return The result of validation containing errors, warnings and info.
    */
   DockerfileLintResult lint();

   /**
    * Return the {@link DockerfileLintResult} ,lint the underlying Dockerfile against the given rule file.
    * 
    * @param ruleFile The {@link Resource} which is the abstraction for YAML rule file used to lint against.
    * @return The result of validation containing errors, warnings and info.
    */
   DockerfileLintResult lint(Resource<?> ruleFile);

   /**
    * Return the {@link DockerDescriptor} representing the underlying Dockerfile.
    * 
    * @return The {@link DockerDescriptor} (ShrinkWrap Descriptors implementation for Dockerfiles) corresponding to this
    *         {@link DockerFileResource}.
    */
   DockerDescriptor getDockerDescriptor();

   /**
    * Sets the content of this {@link DockerFileResource} to the passed {@link DockerDescriptor}.
    * 
    * @param descriptor The {@link DockerDescriptor} (ShrinkWrap Descriptors implementation for Dockerfiles).
    * @return this {@link DockerFileResource} with contents of the descriptor.
    */
   DockerFileResource setContents(DockerDescriptor descriptor);

}
