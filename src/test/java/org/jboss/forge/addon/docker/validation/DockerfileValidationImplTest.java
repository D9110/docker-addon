package org.jboss.forge.addon.docker.validation;

import java.io.File;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.docker.resource.DockerFileResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.arquillian.AddonDependencies;
import org.jboss.forge.arquillian.archive.AddonArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class DockerfileValidationImplTest
{
   @Deployment
   @AddonDependencies
   public static AddonArchive getDeployment()
   {
      return ShrinkWrap
               .create(AddonArchive.class)
               .addBeansXML()
               .add(new FileAsset(new File("src/test/resources/org/jboss/forge/addon/docker/validation/Dockerfile")),
                        "org/jboss/forge/addon/docker/validation/Dockerfile")
               .add(new FileAsset(new File(
                        "src/test/resources/org/jboss/forge/addon/docker/validation/default_rules.yaml")),
                        "org/jboss/forge/addon/docker/validation/default_rules.yaml");
   }

   @Inject
   ResourceFactory resourceFactory;

   @Inject
   DockerfileValidationImpl dockerfileValidationImpl;

   @Test
   public void testDockerfileValidation() throws Exception
   {

      Assert.assertNotNull(getClass().getResource("default_rules.yaml"));
      Assert.assertNotNull(getClass().getResource("Dockerfile"));

      DockerFileResource dockerfileResource = resourceFactory.create(DockerFileResource.class,
               File.createTempFile("Dockerfile", ""));
      dockerfileResource.createNewFile();
      Assert.assertTrue(dockerfileResource.exists());
      dockerfileResource.setContents(getClass().getResource("Dockerfile").openStream());

      @SuppressWarnings("unchecked")
      FileResource<?> ruleFile = resourceFactory.create(FileResource.class, File.createTempFile("rules", "yaml"));
      ruleFile.createNewFile();
      Assert.assertTrue(ruleFile.exists());
      ruleFile.setContents(getClass().getResource("default_rules.yaml").openStream());

      DockerfileValidationResult dockerfileValidationResult = dockerfileValidationImpl.verify(dockerfileResource,
               ruleFile);

      Assert.assertEquals(dockerfileValidationResult.getErrors(), 0);
      Assert.assertEquals(dockerfileValidationResult.getWarn(), 1);
      Assert.assertEquals(dockerfileValidationResult.getInfo(), 2);

      Assert.assertEquals(
               dockerfileValidationResult.toString(),
               "Validation Results\nErrors: 0\nWarn: 1\nInfo: 2\n\nType: INFO\nMessage: There is no \'EXPOSE\' instruction.Without exposed ports how will the service of the container be accessed?.\nReference --> \nhttps://docs.docker.com/reference/builder/#expose\n\nType: INFO\nMessage: There is no \'ENTRYPOINT\' instruction.None.\nReference --> \nhttps://docs.docker.com/reference/builder/#entrypoint\n\nType: WARN\nMessage: No \'USER\' instruction.The process(es) within the container may run as root and RUN instructions my be run as root.\nReference --> \nhttps://docs.docker.com/reference/builder/#user\n");

   }

   @Test
   public void testDockerfileValidationWithNoRuleFile() throws Exception
   {
      Assert.assertNotNull(getClass().getResource("Dockerfile"));

      DockerFileResource dockerfileResource = resourceFactory.create(DockerFileResource.class,
               File.createTempFile("Dockerfile", ""));
      dockerfileResource.createNewFile();
      Assert.assertTrue(dockerfileResource.exists());
      dockerfileResource.setContents(getClass().getResource("Dockerfile").openStream());

      DockerfileValidationResult dockerfileValidationResult = dockerfileValidationImpl.verify(dockerfileResource);

      Assert.assertEquals(dockerfileValidationResult.getErrors(), 0);
      Assert.assertEquals(dockerfileValidationResult.getWarn(), 0);
      Assert.assertEquals(dockerfileValidationResult.getInfo(), 0);

      Assert.assertEquals(
               dockerfileValidationResult.toString(),
               "Validation Results\nErrors: 0\nWarn: 0\nInfo: 0\n");
      
      //This should work ideally
      dockerfileResource.verify();
   }
}
