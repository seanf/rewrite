/*
 * Copyright 2011 <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ocpsoft.rewrite.servlet.validate;

import javax.servlet.ServletContext;

import com.ocpsoft.rewrite.bind.Evaluation;
import com.ocpsoft.rewrite.bind.Validator;
import com.ocpsoft.rewrite.config.Configuration;
import com.ocpsoft.rewrite.config.ConfigurationBuilder;
import com.ocpsoft.rewrite.config.Direction;
import com.ocpsoft.rewrite.context.EvaluationContext;
import com.ocpsoft.rewrite.event.Rewrite;
import com.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import com.ocpsoft.rewrite.servlet.config.Path;
import com.ocpsoft.rewrite.servlet.config.SendStatus;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class BindingValidationTestProvider extends HttpConfigurationProvider
{
   @Override
   public int priority()
   {
      return 0;
   }

   @SuppressWarnings("rawtypes")
   @Override
   public Configuration getConfiguration(final ServletContext context)
   {
      Configuration config = ConfigurationBuilder
               .begin()
               .defineRule()
               .when(Direction.isInbound().and(
                        Path.matches("/v/{param}").where("param")
                                 .bindsTo(Evaluation.property("param").validatedBy(new Validator() {
                                    @Override
                                    public boolean validate(final Rewrite event, final EvaluationContext context,
                                             final Object value)
                                    {
                                       return "valid".equals(value);
                                    }
                                 }))))
               .perform(SendStatus.code(205))

               .defineRule()
               .when(Direction.isInbound().and(Path.matches("/v/{param}").where("param").matches("[a-zA-Z]+")))
               .perform(SendStatus.code(206));

      return config;
   }

}
