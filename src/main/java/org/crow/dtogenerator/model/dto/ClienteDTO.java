package org.crow.dtogenerator.model.dto

public class ClienteDTO
{
  private String nome;
  private String telefoneFixo;
  private String veiculo;
  
  public String getNome()
  {
    return this.nome;
  }
  
  public void setNome(String paramString)
  {
    this.nome = paramString;
  }
  
  public String getTelefoneFixo()
  {
    return this.telefoneFixo;
  }
  
  public void setTelefoneFixo(String paramString)
  {
    this.telefoneFixo = paramString;
  }
  
  public String getVeiculo()
  {
    return this.veiculo;
  }
  
  public void setVeiculo(String paramString)
  {
    this.veiculo = paramString;
  }
}
